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
  images: {
    unoptimized: true,
  },
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        headers: [
          { key: "Access-Control-Allow-Credentials", value: "true" },
          { key: "Access-Control-Allow-Origin", value: "*" },
          {
            key: "Access-Control-Allow-Methods",
            value: "GET,OPTIONS,PATCH,DELETE,POST,PUT",
          },
          {
            key: "Access-Control-Allow-Headers",
            value:
              "X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version",
          },
        ],
        destination: BASE_URL + "/:path*",
      },
    ];
  },
};

module.exports = removeImports({
  ...nextConfig,
});
