import type { AppProps } from "next/app";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { RecoilRoot } from "recoil";
import Header from "components/Header/Header";
import Footer from "components/Footer/Footer";
import Navbar from "components/Navbar/Navbar";
import "../styles/globals.css";

export default function App({ Component, pageProps }: AppProps) {
  const queryClient = new QueryClient();

  return (
    <>
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
    </>
  );
}
