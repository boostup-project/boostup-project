import { BasicInfo } from "components/createModal/BasicInfo";
import Head from "next/head";

interface Props {
  metaInfo?: BasicInfo;
  url?: string;
  metaType:
    | "Home"
    | "Lesson"
    | "Mypage"
    | "Pay"
    | "Receipt"
    | "Chat"
    | "Signup"
    | "Login"
    | "ResetPassword";
}

const RepeatedMeta = ({ url }: { url: string }) => {
  return (
    <>
      <meta
        name="description"
        content="코딩 과외 매칭 플랫폼 코듀온, Codueon"
      ></meta>
      {/* Facebook Meta Tags  */}
      <meta property="og:title" content="코듀온" />
      <meta property="og:url" content={url} />
      <meta property="og:type" content="website" />
      <meta property="og:image" content="images/logo.png" />
      <meta
        property="og:description"
        content="코딩 과외 매칭 플랫폼 코듀온, Codueon"
      />
      {/* Twitter Meta Tags  */}
      <meta name="twitter:card" content="images/logo.png" />
      <meta property="twitter:url" content={url} />
      <meta name="twitter:title" content="코듀온" />
      <meta
        name="twitter:description"
        content="코딩 과외 매칭 플랫폼 코듀온, Codueon"
      />
      <meta name="twitter:image" content="/images/logo.png" />
    </>
  );
};

/**
 * @metaInfo lesson page SEO required data
 * @url string
 * @metaType | "Home" | "Lesson" | "Mypage" | "Pay" | "Receipt" | "Chat" | "Signup" | "Login" | "ResetPassword"; */
const SeoHead = ({ metaInfo, url, metaType }: Props) => {
  return (
    <Head>
      <meta charSet="utf-8" />
      <link rel="icon" href="images/favicon-logo.ico" />
      {/* Home */}
      {metaType === "Home" && (
        <>
          <title>코딩 과외 매칭 플랫폼 | 코듀온</title>
          <RepeatedMeta url="https://www.codueon.com/" />
        </>
      )}
      {/* Lesson */}
      {metaType === "Lesson" && (
        <>
          <title>{`${metaInfo!.title} | 코듀온`}</title>
          <meta name="description" content={metaInfo!.title.toString()}></meta>
          {/* Facebook Meta Tags  */}
          <meta property="og:title" content={`${metaInfo!.title} | 코듀온`} />
          <meta
            property="og:url"
            content={`https://www.codueon.com/lesson/${metaInfo!.id}`}
          />
          <meta property="og:type" content="website" />
          <meta
            property="og:image"
            content={metaInfo!.profileImage.toString()}
          />
          <meta
            property="og:description"
            content="코딩 과외 매칭 플랫폼 코듀온, Codueon"
          />
          {/* Twitter Meta Tags  */}
          <meta name={`${metaInfo!.title} | 코듀온`} content="코듀온" />
          <meta name="twitter:card" content="images/logo.png" />
          <meta
            property="twitter:url"
            content={`https://www.codueon.com/lesson/${metaInfo!.id}`}
          />
          <meta
            name="twitter:description"
            content="코딩 과외 매칭 플랫폼 코듀온, Codueon"
          />
          <meta
            name="twitter:image"
            content={metaInfo!.profileImage.toString()}
          />
        </>
      )}
      {/* Mypage */}
      {metaType === "Mypage" && (
        <>
          <title>{`마이페이지 | 코듀온`}</title>
          <RepeatedMeta url={`https://www.codueon.com/mypage/${url}`} />
        </>
      )}
      {/* Pay */}
      {metaType === "Pay" && (
        <>
          <title>{`결제정보 | 코듀온`}</title>
          <RepeatedMeta url={`https://www.codueon.com/shop/${url}`} />
        </>
      )}
      {/* Receipt */}
      {metaType === "Receipt" && (
        <>
          <title>{`결제 영수증 | 코듀온`}</title>
          <RepeatedMeta url={`https://www.codueon.com/shop/${url}/receipt`} />
        </>
      )}
      {/* Chat */}
      {metaType === "Chat" && (
        <>
          <title>{`실시간채팅 | 코듀온`}</title>
          <RepeatedMeta url={`https://www.codueon.com/chat/${url}`} />
        </>
      )}
      {/* Signup */}
      {metaType === "Signup" && (
        <>
          <title>{`회원가입 | 코듀온`}</title>
          <RepeatedMeta url={`https://www.codueon.com/signup`} />
        </>
      )}
      {/* Login */}
      {metaType === "Login" && (
        <>
          <title>{`로그인 | 코듀온`}</title>
          <RepeatedMeta url={`https://www.codueon.com/login`} />
        </>
      )}
      {/* ResetPassword */}
      {metaType === "ResetPassword" && (
        <>
          <title>비밀번호 찾기 | 코듀온</title>
          <RepeatedMeta url="https://www.codueon.com/resetPassword" />
        </>
      )}
    </Head>
  );
};

export default SeoHead;
