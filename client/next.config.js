/** @type {import('next').NextConfig} */
const removeImports = require("next-remove-imports")();

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

const nextConfig = {
  reactStrictMode: false,
  swcMinify: true,
  //   images:{
  //     loader: 'imgix',
  //     path: '/',
  //   },
  // images: {
  //   unoptimized: true,
  // },
  images: {
    domains: ["codueon.s3.ap-northeast-2.amazonaws.com"],
  },
  async rewrites() {
    return [
      {
        source: "/apis/:path*",
        destination: BASE_URL + "/:path*",
      },
    ];
  },
};

module.exports = removeImports({
  ...nextConfig,
});
