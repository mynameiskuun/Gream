package com.project.gream.domain.order.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.OrderState;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.member.service.MemberService;
import com.project.gream.domain.order.dto.*;
import com.project.gream.domain.order.entity.OrderHistory;
import com.project.gream.domain.order.entity.OrderItem;
import com.project.gream.domain.order.repository.OrderHistoryRepository;
import com.project.gream.domain.order.repository.OrderItemRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${kakaopay.cid}")
    private String cid;
    @Value("${kakaopay.adminKey}")
    private String adminKey;
    private final String kakaoPayLocalUrl = "http://localhost:9999";
    private final String kakaoPayRealUrl = "http://ec2-3-36-228-95.ap-northeast-2.compute.amazonaws.com:8080";
    public static final String ePw = createKey(); // 회원가입 인증, 비밀번호 변경 등 기능 추가시 사용
    private final JavaMailSender emailSender;
    private final HttpSession session;
    private final MemberRepository memberRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MemberService memberService;

    @Override
    public KakaoPayRequestVO kakaoPayReady(KakaoPayDto request) {

        log.info("cid : " + cid);
        session.setAttribute("size", request.getSize());
        session.setAttribute("itemName", request.getItemName());

        String orderId = this.getItemName(request);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", orderId);
        parameters.add("partner_user_id", "Gream");
        parameters.add("item_name", request.getItemName());
        parameters.add("quantity", String.valueOf(request.getSize()));
        parameters.add("total_amount", String.valueOf(request.getFinalPaymentAmount()));
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", kakaoPayLocalUrl + "/order/kakaopay/authorization"); // 결제승인시 넘어갈 url
        parameters.add("cancel_url", kakaoPayLocalUrl + "/order/kakaopay/cancel"); // 결제취소시 넘어갈 url
        parameters.add("fail_url", kakaoPayLocalUrl + "/order/kakaopay/fail"); // 결제 실패시 넘어갈 url

        log.info("--------------------- 결제준비 parameters : " + parameters);

        String requestUrl = "https://kapi.kakao.com/v1/payment/ready";

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        KakaoPayRequestVO requestVO = new RestTemplate().postForObject(requestUrl, requestEntity, KakaoPayRequestVO.class);

        log.info("-------------------------- 결제준비 응답객체: " + requestVO);
        // 받아온 값 return
        return requestVO;
    }

    public String getItemName(KakaoPayDto request) {
        String itemName = request.getItemName();
        return request.getSize() == 1 ? itemName : itemName + " 외 " + (request.getSize() - 1) + "개";
    }

    public HttpHeaders getHeaders() {

        HttpHeaders headers = new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }

    @Override
    public KakaoPayApprovedResultVO payApprove(String tid, String pgToken, KakaoPayDto request) {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", cid);
        parameters.add("tid", tid);
        parameters.add("partner_order_id", this.getItemName(request)); // 주문명
        parameters.add("partner_user_id", "Gream");
        parameters.add("pg_token", pgToken);

        log.info("--------------------------------결제승인전 parameters" + parameters);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 결제 승인 시 외부 url 통신
        String url = "https://kapi.kakao.com/v1/payment/approve";

        KakaoPayApprovedResultVO kakaoPayApprovedResultVO = new RestTemplate().postForObject(url, requestEntity, KakaoPayApprovedResultVO.class);
        log.info("----------------------------- 응답객체 : " + kakaoPayApprovedResultVO);

        return kakaoPayApprovedResultVO;
    }

    @Transactional
    @Override
    public void updateDataBaseAfterPayment(KakaoPayDto kakaoPayDto, @LoginMember MemberDto memberDto) {

        log.info("---------------------- DB 업데이트 시작");

        OrderHistory orderHistory = this.saveOrderhistory(kakaoPayDto, memberDto);
        this.saveOrderItems(kakaoPayDto, orderHistory);

        itemService.updateItemStock(kakaoPayDto, orderHistory);
        memberService.updateCartItems(kakaoPayDto);
    }

    public OrderHistory saveOrderhistory(KakaoPayDto kakaoPayDto, MemberDto memberDto) {

        log.info("--------------------- 주문내역 저장");
        Member member = memberRepository.findById(memberDto.getId()).orElseThrow();

        OrderHistory orderHistory = OrderHistory.builder()
                .member(member)
                .usePoint(kakaoPayDto.getUsePoint())
                .totalOrderPrice(kakaoPayDto.getFinalPaymentAmount())
                .build();

        orderHistoryRepository.save(orderHistory);

        return orderHistory;
    }

    public void saveOrderItems(KakaoPayDto kakaoPayDto, OrderHistory orderHistory) {

        log.info("------------------------ 주문상품 저장");

//        String[] itemArray = kakaoPayDto.getItemIds().split("/");
//        String[] qtyArray = kakaoPayDto.getItemQtys().split("/");

//        for (int i = 0; i<itemArray.length; i++) {
//            Item item = itemRepository.findById(Long.valueOf(itemArray[i]))
//                    .orElseThrow(() -> new NoSuchElementException());
//
//            OrderItem orderItem = OrderItem.builder()
//                    .orderHistory(orderHistory)
//                    .quantity(Integer.parseInt(qtyArray[i]))
//                    .state("배송 준비중")
//                    .totalPrice(item.getPrice() * Integer.parseInt(qtyArray[i]))
//                    .item(item)
//                    .build();
//
//            orderItemRepository.save(orderItem);
//        }

        List<Long> itemIdArray = kakaoPayDto.getItemIds();
        List<Integer> qtyArray = kakaoPayDto.getItemQtys();
        int itemIdArraySize = itemIdArray.size();

        for (int i = 0; i < itemIdArraySize; i++) {
            Item item = itemRepository.findById(itemIdArray.get(i))
                    .orElseThrow(() -> new NoSuchElementException());

            OrderItem orderItem = OrderItem.builder()
                    .orderHistory(orderHistory)
                    .quantity(qtyArray.get(i))
                    .state("배송 준비중")
                    .totalPrice(item.getPrice() * qtyArray.get(i))
                    .item(item)
                    .build();

            orderItemRepository.save(orderItem);
        }
    }


    @Override
    public void sendPaymentReceiptEmail(KakaoPayApprovedResultVO result, MemberDto memberDto) throws Exception {

        log.info("----------------------- 결제내역 이메일 발송");
        MimeMessage message = this.createOrderHistoryMessage(result, memberDto);
        emailSender.send(message);
    }

    private MimeMessage createOrderHistoryMessage(KakaoPayApprovedResultVO result, MemberDto memberDto) throws Exception {
        log.info("보내는 대상 : " + memberDto.getEmail());
//        log.info("인증 번호 : " + ePw);

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, memberDto.getEmail()); // 보내는 대상
        message.setSubject(memberDto.getName() + " 님의 결제 내역"); // 제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1>" + memberDto.getName() + " 님의 구매 내역입니다. </h1>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<div><span><h4>상품명</h4></span><span> " + result.getItem_name() + "</span></div>";
        msgg += "<br>";
        msgg += "<div><span><h4>결제 수단</h4></span><span> " + result.getPayment_method_type() + "</span></div>";
        msgg += "<br>";
        msgg += "<div><span><h4>결제 금액</h4></span><span> " + result.getAmount().getTotal() + "</span></div>";
        msgg += "<br>";
        msgg += "<div><span><h4>할인 금액</h4></span><span> " + result.getAmount().getDiscount() + "</span></div>";
        msgg += "<br>";
        msgg += "<div><span><h4>결제 승인 시각</h4></span><span> " + result.getApproved_at() + "</span></div>";
        msgg += "<br>";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("accformanager99@gmail.com", "ADMIN"));//보내는 사람

        return message;
    }

    @Override
    public List<OrderItemDto> findOrderItemForMypage(String memberId) {

        log.info("------------------------------ 회원 아이디로 주문한 상품 출력 - 마이페이지");
        List<OrderItem> orderItems = orderItemRepository.findTop4ByOrderHistory_Member_IdOrderByCreatedTimeDesc(memberId);

        return orderItems.stream()
                .map(OrderItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderItemDto> findAllOrderItem(String memberId, Pageable pageable) {

        log.info("------------------------------ 회원 아이디로 주문한 상품 출력");
//        orderItemRepository.findAllByOrderHistory_Member_Id(memberId, pageable);

        return orderItemRepository.findAllByOrderHistory_Member_Id(memberId, pageable).map(OrderItem -> OrderItemDto.builder()
                .id(OrderItem.getId())
                .quantity(OrderItem.getQuantity())
                .totalPrice(OrderItem.getTotalPrice())
                .state(OrderItem.getState())
                .itemDto(ItemDto.fromEntity(OrderItem.getItem()))
                .orderHistoryDto(OrderHistoryDto.fromEntity(OrderItem.getOrderHistory()))
                .createdTime(OrderItem.getCreatedTime())
                .modifiedTime(OrderItem.getModifiedTime()).build());
    }

    public static String createKey() {
        return String.valueOf(System.nanoTime());
    }

    @Override
    public List<OrderItemDto> sortByOrderState(String sortBy, MemberDto memberDto) {
        String state = EnumSet.allOf(OrderState.class).stream()
                .filter(c -> c.name().equals(sortBy))
                .findAny()
                .get()
                .getValue();

        return orderItemRepository.findAllByStateAndOrderHistory_Member(state, memberDto.toEntity()).stream()
                .map(OrderItemDto::fromEntity)
                .collect(Collectors.toList());
    }
}
