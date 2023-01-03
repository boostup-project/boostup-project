package com.codueon.boostUp.domain.suggest.service;

import com.codueon.boostUp.domain.lesson.entity.Lesson;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.domain.suggest.dto.GetSuggestInfo;
import com.codueon.boostUp.domain.suggest.dto.PostReason;
import com.codueon.boostUp.domain.suggest.dto.PostSuggest;
import com.codueon.boostUp.domain.suggest.entity.PaymentInfo;
import com.codueon.boostUp.domain.suggest.entity.Reason;
import com.codueon.boostUp.domain.suggest.entity.Suggest;
import com.codueon.boostUp.domain.suggest.pay.KakaoPayHeader;
import com.codueon.boostUp.domain.suggest.pay.PayReadyInfo;
import com.codueon.boostUp.domain.suggest.pay.ReadyToPaymentInfo;
import com.codueon.boostUp.domain.suggest.response.Message;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codueon.boostUp.domain.suggest.utils.SuggestConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuggestService {

    private final SuggestDbService suggestDbService;
    private final LessonDbService lessonDbService;
    private final MemberDbService memberDbService;

    private final FeignService feignService;

    @Transactional
    public void createSuggest(PostSuggest post, Long lessonId, Long memberId) {

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);

//        if(memberId.equals(findLesson.getMemberId())) {
//            throw new BusinessLogicException(ExceptionCode.TUTOR_CANNOT_RESERVATION);
//        }

        Suggest suggest = Suggest.builder()
                .days(post.getDays())
                .languages(post.getLanguages())
                .requests(post.getRequests())
                .lessonId(lessonId)
                .memberId(memberId)
                .build();

        suggest.setStatus(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS);

        suggestDbService.saveSuggest(suggest);
    }

    @Transactional
    public void acceptSuggest(Long lessonId, Long suggestId, Long memberId, Integer quantity) {

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);

        if (!memberId.equals(findLesson.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!findSuggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        findSuggest.setQuantity(quantity);
        findSuggest.setStartTime();
        findSuggest.setTotalCost(findLesson.getCost() * findSuggest.getQuantity());
        findSuggest.setStatus(Suggest.SuggestStatus.PAY_IN_PROGRESS);

        suggestDbService.saveSuggest(findSuggest);

    }

    public void cancelSuggest(Long suggestId, Long memberId) {

        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!memberId.equals(findSuggest.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        if (!findSuggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS) &&
                !findSuggest.getStatus().equals(Suggest.SuggestStatus.PAY_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        suggestDbService.deleteSuggest(findSuggest);
    }

    public void declineSuggest(Long lessonId, Long suggestId, Long memberId, PostReason postReason) {

        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);

        if (!memberId.equals(findLesson.getMemberId()) ||
                !findSuggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        Reason reason = Reason.builder().reason(postReason.getReason()).build();
        suggestDbService.saveReason(reason);

        suggestDbService.deleteSuggest(findSuggest);

    }

    public GetSuggestInfo getSuggestInfo(Long suggestId, Long lessonId, Long memberId) {

        Lesson findLesson = lessonDbService.ifExistsReturnLesson(lessonId);
        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);

        if (!memberId.equals(findSuggest.getMemberId()) ||
            findSuggest.getStatus().equals(Suggest.SuggestStatus.ACCEPT_IN_PROGRESS)) {
            throw new BusinessLogicException(ExceptionCode.INVALID_ACCESS);
        }

        return new GetSuggestInfo(findLesson, findSuggest.getTotalCost(), findSuggest.getQuantity());
    }

    public Message getKaKapPayUrl(Long suggestId, Long memberId, String requestUrl) {

        Suggest findSuggest = suggestDbService.ifExistsReturnSuggest(suggestId);
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        Lesson findLesson = lessonDbService.ifExistsReturnLesson(findSuggest.getLessonId());

        KakaoPayHeader headers = feignService.setHeaders();
        ReadyToPaymentInfo params = feignService.setParams(requestUrl, findSuggest, findMember, findLesson);

        PayReadyInfo payReadyInfo = feignService.getPayReadyInfo(headers, params);

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .params(params)
                .tid(payReadyInfo.getTid())
                .build();

        suggestDbService.savePayment(paymentInfo);

        return Message.builder()
                .data(payReadyInfo.getNextRedirectPcUrl())
                .message(PAY_URI_MSG)
                .build();

    }

    public Message<?> getFailedPayMessage() {
        return Message.builder()
                .message(FAILED_INFO_MESSAGE + "<br>" + INVALID_PARAMS)
                .build();
    }


}
