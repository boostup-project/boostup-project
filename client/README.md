# 🚀 BoostUp Front-end

## Quick Start


```shell=
git clone https://github.com/boostup-project/boostup-project.git
npm install
npm run dev
```
### Required Enviornment Variables
```javascript=
NEXT_PUBLIC_API_URL=${server_domain}
// For OAuth
GOOGLE_CLIENT_SECRET
NEXTAUTH_URL
JWT_SECRET
GOOGLE_CLIENT_ID
```

<br />

## 📌 Key Stacks

- TypeScript
    - 타입의 안정성을 활용하고자 사용했습니다.
- Next.js
    - SEO를 충족을 위한 프레임워크이기에 사용했습니다.
- Tailwind CSS
    - 반응형 웹페이지를 위한 preset을 쉽게 설정 및 활용할 수 있어서 사용했습니다.
- Recoil
    - 로그인 정보 및 일부 컴포넌트의 특성상 useQuery의 invalidateQueries옵션을 사용할 수 없는 컴포넌트의 refetch를 제어하기위해 사용했습니다.
- Axios
    - HTTP 통신을 하기 위하여 사용했습니다.
- TanStack Query
    - 서비스가 실제로 운영된다고 생각했을 때, 서버의 트래픽을 최소화시켜주는 것이 비용, 성능적인 측면에서 중요하다고 생각했고, 데이터를 캐싱하여 stale한 경우만 재요청할 수 있는 TanStack Query를 사용했습니다.
- eslint, prettier
    - 코드 정렬 및 컨벤션을 위해 사용했습니다.
- React-Toastify
    - 사용자 행동에 따른 결과를 고지하기 위해 사용했습니다.
- SweetAlert2
    - 사용자 행동 중 한 번 더 확인이 필요한 중요한 액션을 위해 사용했습니다.
- STOMP.js
    - 과외 선생님과 과외 수강생들의 실시간 대화를 구현하기 위해 사용했습니다.
- React Hook Form
    - 내부적으로 useRef를 통한 [렌더링 최적화 및 dependancy가 적으면서](https://blog.logrocket.com/react-hook-form-vs-formik-comparison/) 유효성 검사 코드의 간결성을 위해 사용했습니다.
- NextAuth.js
    - OAuth 관련 package중 Next.js와 좋은 호환성([designed from the ground up to support Next.js and Serverless.](https://next-auth.js.org/getting-started/introduction))을 가지기에 사용했습니다.

<br />

## 📌 Deployed via...

- Vercel + Route53
    - Next.js를 만든 곳이기에 호환성이 좋고, amplify에 의한 배포가 느리다는 [nextjs discussion](https://github.com/aws-amplify/amplify-hosting/issues/2127)을 확인했습니다.
    - 커스텀 도메인을 이미 구매한 상태에서 Route53을 이용했습니다.

## 📌 File Structure

```
📦client
 ┣ 📂public
 ┃ ┣ 📂fonts # 폰트 폴더
 ┃ ┣ 📂images # 사용된 png이미지들의 폴더
 ┣ 📂apis # axios를 이용한 api 통신
 ┣ 📂assets # color상수와 컴포넌트화 시킨 svg 아이콘을 보관하는 폴더
 ┣ 📂atoms # recoil의 atom 보관
 ┣ 📂components # 컴포넌트 파일
 ┣ 📂hooks # apis폴더에 있는 통신 코드를 tanstack-query로 감싸서 저장한 폴더
 ┣ 📂pages # routing되는 페이지가 모여있는 폴더
 ┣ 📂styles # global.css를 보관하는 폴더
 ┣ 📜.eslintrc.json
 ┣ 📜.gitignore
 ┣ 📜.prettierrc.js
 ┣ 📜package-lock.json
 ┣ 📜package.json
 ┣ 📜README.md
 ┣ 📜tailwind.config.js
 ┗ 📜tsconfig.json
```

<br />

