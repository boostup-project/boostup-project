package com.codueon.boostUp.domain.lesson.entity;

import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum AddressInfo {
    GANGNAMGU(1, "강남구"), GANGDONGGU(2, "강동구"), GANGBUKGU(3, "강북구"),
    GANGSEOGU(4, "강서구"), GWANAKGU(5, "관악구"), GWANGJINGU(6, "광진구"),
    GUROGU(7, "구로구"), GEUMCHEONGU(8, "금천구"), NOWONGU(9, "노원구"),
    DOBONGGU(10, "도봉구"), DONGDAEMUNGU(11, "동대문구"), DONGJAKGU(12, "동작구"),
    MAPOGU(13, "마포구"), SEODAEMUNGU(14, "서대문구"), SEOCHOGU(15, "서초구"),
    SEONGDONGGU(16, "성동구"), SEONGBUKGU(17, "성북구"), SONGPAGU(18, "송파구"),
    YANGCHEONGU(19, "양천구"), YEONGDEUNGPOGU(20, "영등포구"), YONGSANGU(21, "용산구"),
    EUNPYEONGGU(22, "은평구"), JONGNOGU(23, "종로구"), JUNGGU(24, "중구"),
    JUNGNANGGU(25, "중랑구");

    private final Integer status;
    private final String address;

    AddressInfo(Integer status, String address) {
        this.status = status;
        this.address = address;
    }

    private static final Map<Integer, AddressInfo> descriptions =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(AddressInfo::getStatus, Function.identity())));

    public static AddressInfo findById(Integer addressId) {
        return Optional.ofNullable(descriptions.get(addressId))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ADDRESS_NOT_FOUND));
    }
}
