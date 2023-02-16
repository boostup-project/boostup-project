import "../styles/globals.css";
import type { AppProps } from "next/app";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { RecoilRoot } from "recoil";
import Header from "components/Header/Header";
import Footer from "components/Footer/Footer";
import Navbar from "components/Navbar/Navbar";
import { useEffect } from "react";
import { useRouter } from "next/router";
import { SessionProvider } from "next-auth/react";

export default function App({ Component, pageProps }: AppProps) {
  const router = useRouter();
  const queryClient = new QueryClient();

  useEffect(() => {
    router.push(window.location.href);
  }, []);

  return (
    <>
      <SessionProvider session={pageProps.session}>
        <QueryClientProvider client={queryClient}>
          <RecoilRoot>
            <div className="flex flex-col max-h-full justify-center items-center bg-bgColor">
              <Header />
              <div className="flex flex-col justify-center items-center desktop:max-w-[1139px] w-full h-full bg-bgColor">
                <Component {...pageProps} />
              </div>
              <Footer />
              <Navbar />
            </div>
          </RecoilRoot>
        </QueryClientProvider>
      </SessionProvider>
    </>
  );
}
