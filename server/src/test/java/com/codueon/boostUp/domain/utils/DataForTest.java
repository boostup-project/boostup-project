package com.codueon.boostUp.domain.utils;

import com.codueon.boostUp.domain.bookmark.entity.Bookmark;
import com.codueon.boostUp.domain.lesson.entity.*;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.entity.MemberImage;
import com.codueon.boostUp.domain.reveiw.entity.Review;
import com.codueon.boostUp.domain.suggest.entity.Suggest;

import java.util.List;

public class DataForTest {

    public MemberImage getMemberImage() {
        return MemberImage.builder()
                .fileName("하이하이")
                .filePath("https://test.com/memberImage/test.jpg")
                .fileSize(50000L)
                .originFileName("뚱인데요")
                .build();
    }

    public ProfileImage getProfileImage() {
        return ProfileImage.builder()
                .fileName("하이하이")
                .filePath("https://test.com/profileImage/test.jpg")
                .fileSize(50000L)
                .originFileName("뚱인데요")
                .build();
    }

    public List<CareerImage> getCareerImages() {
        return List.of(
                CareerImage.builder()
                        .id(1L)
                        .fileName("하이하이1")
                        .filePath("https://test.com/careerImage/test1.jpg")
                        .fileSize(50000L)
                        .originFileName("뚱인데요1")
                        .build(),
                CareerImage.builder()
                        .id(2L)
                        .fileName("하이하이2")
                        .filePath("https://test.com/careerImage/test2.jpg")
                        .fileSize(50000L)
                        .originFileName("뚱인데요2")
                        .build(),
                CareerImage.builder()
                        .id(3L)
                        .fileName("하이하이3")
                        .filePath("https://test.com/careerImage/test3.jpg")
                        .fileSize(50000L)
                        .originFileName("뚱인데요3")
                        .build()
        );
    }

    public Member getMember1() {
        Member member = Member.builder()
                .id(1L)
                .name("김길동")
                .email("gddong@gmail.com")
                .password("ghdrlfehd1!")
                .roles(List.of("USER"))
                .build();
        member.addMemberImage(getMemberImage());
        return member;
    }

    public Member getMember2() {
        Member member = Member.builder()
                .id(2L)
                .name("조길동")
                .email("jddong@gmail.com")
                .password("ghdrlfehd1!")
                .roles(List.of("USER"))
                .build();
        member.addMemberImage(getMemberImage());
        return member;
    }

    public List<Integer> getAddressList() {
        return List.of(
                1, 2, 3
        );
    }

    public List<Integer> getLanguageList() {
        return List.of(
                1, 2, 3
        );
    }

    public Lesson getLesson1() {
        Lesson lesson = Lesson.builder()
                .id(1L)
                .title("자바 숙성 강의")
                .cost(5000)
                .memberId(1L)
                .company("배달의 민족")
                .career(100)
                .build();

        List<Integer> addressList = getAddressList();
        List<Integer> languageList = getLanguageList();
        addressList.forEach(id ->
                lesson.addLessonAddress(LessonAddress.builder()
                        .addressId(id)
                        .build()));
        languageList.forEach(id ->
                lesson.addLessonLanguage(LessonLanguage.builder()
                        .languageId(id)
                        .build()));

        lesson.addProfileImage(getProfileImage());

        return lesson;
    }

    public Lesson getLesson2() {
        Lesson lesson = Lesson.builder()
                .id(2L)
                .title("자바 스크립트 숙성 강의")
                .cost(5000)
                .memberId(2L)
                .company("코코넛 엔터프라이즈")
                .career(100)
                .build();

        List<Integer> addressList = getAddressList();
        List<Integer> languageList = getLanguageList();
        addressList.forEach(id ->
                lesson.addLessonAddress(LessonAddress.builder()
                        .addressId(id)
                        .build()));
        languageList.forEach(id ->
                lesson.addLessonLanguage(LessonLanguage.builder()
                        .languageId(id)
                        .build()));

        lesson.addProfileImage(getProfileImage());

        return lesson;
    }

    public List<Lesson> getLessonList() {
        return List.of(getLesson1(), getLesson2());
    }

    public LessonInfo getLessonInfo1() {
        LessonInfo lessonInfo = LessonInfo.builder()
                .id(1L)
                .lessonId(1L)
                .introduction("맞으면서 배워봐요!")
                .companies("안우아한 형제들 3년, 카카오 엔터프라이즈 2년")
                .favoriteLocation("스타벅스(맥북 필수)")
                .personality("착할 수도")
                .costs("Java 3만원, JavaScript 10만원, Python 6만원")
                .build();

        List<CareerImage> careerImageList = getCareerImages();
        careerImageList.forEach(lessonInfo::addCareerImage);
        return lessonInfo;
    }

    public LessonInfo getLessonInfo2() {
        LessonInfo lessonInfo = LessonInfo.builder()
                .id(2L)
                .lessonId(2L)
                .introduction("틀리면 한 대씩!")
                .companies("안우아한 형제들 2년, 카카오 엔터프라이즈 3년")
                .favoriteLocation("투썸 플레이스")
                .personality("나쁩니다.")
                .costs("Java 3만원, JavaScript 10만원, Python 6만원")
                .build();

        List<CareerImage> careerImageList = getCareerImages();
        careerImageList.forEach(lessonInfo::addCareerImage);
        return lessonInfo;
    }

    public Curriculum getCurriculum1() {
        return Curriculum.builder()
                .id(1L)
                .curriculum("주입식 빠따 교육으로 갑니다.")
                .lessonId(1L)
                .build();
    }

    public Curriculum getCurriculum2() {
        return Curriculum.builder()
                .id(2L)
                .curriculum("주입식 빠따 빠따 교육으로 갑니다.")
                .lessonId(2L)
                .build();
    }

    public Review getReview1() {
        return Review.builder()
                .id(1L)
                .score(4)
                .comment("과외가 끔찍했어요!")
                .memberId(1L)
                .lessonId(1L)
                .build();
    }

    public Review getReview2() {
        return Review.builder()
                .id(2L)
                .score(3)
                .comment("과외가 재밌었어요!")
                .memberId(2L)
                .lessonId(1L)
                .build();
    }

    public List<Review> getReviewList() {
        return List.of(getReview1(), getReview2());
    }

    public Suggest getSuggest1() {
        return Suggest.builder()
                .id(1L)
                .lessonId(1L)
                .memberId(1L)
                .days("월, 수, 금")
                .languages("Java")
                .requests("누워서 수업 들어도 되나요?")
                .totalCost(200000)
                .paymentMethod("카카오페이")
                .build();
    }

    public Suggest getSuggest2() {
        return Suggest.builder()
                .id(2L)
                .lessonId(1L)
                .memberId(2L)
                .days("월, 수, 금")
                .languages("Javascript")
                .requests("일어나서 수업 들어도 되나요?")
                .totalCost(200000)
                .paymentMethod("카카오페이")
                .build();
    }

    public List<Suggest> getSuggestList() {
        return List.of(getSuggest1(), getSuggest2());
    }

    public Bookmark getBookmark1() {
        return Bookmark.builder()
                .id(1L)
                .lessonId(1L)
                .memberId(1L)
                .build();
    }

    public Bookmark getBookmark2() {
        return Bookmark.builder()
                .id(2L)
                .lessonId(2L)
                .memberId(1L)
                .build();
    }

    public List<Bookmark> getBookmarkList() {
        return List.of(getBookmark1(), getBookmark2());
    }
}
