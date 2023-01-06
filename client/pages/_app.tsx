import "../styles/globals.css";
import type { AppProps } from "next/app";
import { QueryClient, QueryClientProvider } from "react-query";
import { RecoilRoot } from "recoil";
import Header from "components/Header/Header";

export default function App({ Component, pageProps }: AppProps) {
  const queryClient = new QueryClient();

  return (
    <>
      <QueryClientProvider client={queryClient}>
        <RecoilRoot>
          <div className="flex flex-col justify-center items-center bg-bgColor">
            <Header />
            <Component {...pageProps} />
          </div>
        </RecoilRoot>
      </QueryClientProvider>
    </>
  );
}
