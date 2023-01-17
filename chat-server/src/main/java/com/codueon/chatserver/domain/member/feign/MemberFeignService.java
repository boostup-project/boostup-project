package com.codueon.chatserver.domain.member.feign;

import com.codueon.chatserver.domain.member.entity.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "member", url = "http://localhost:8080")
public interface MemberFeignService {

    Member ifExistsReturnMember(@RequestHeader String authorization,
                                @RequestHeader String accept,
                                @RequestHeader String contentType,
                                @RequestBody Long memberId);
}
