import React from "react";
import Link from "next/link";

import Slider from "react-slick";
import Image from "next/image";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
// import carouselImg from "../../public/images/temp_image.png";

const Carouselcomp = () => {
  const settings = {
    dots: true,
    infinite: true,
    autoplay: true,
    speed: 800,
    autoplaySpeed: 5000,
    cssEase: "linear",
    centerMode: false,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
  };
  return (
    <>
      <div className="w-full desktop:w-3/4 desktop:min-w-[1000px] h-fit  mt-4 rounded-xl">
        {/* <Image src={"/images/temp_image.png"}  width={250} height={100} alt="photo1" priority={true} /> */}
        <Slider {...settings} className="w-full h-full">
          <div className="flex items-center justify-center w-full h-[250px] rounded-xl bg-cover bg-[url('/images/carousel/1.png')]"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-cover bg-[url('/images/carousel/2.png')] rounded-xl"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-cover bg-[url('/images/carousel/3.png')] rounded-xl"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-cover bg-[url('/images/carousel/4.png')] rounded-xl"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-cover bg-[url('/images/carousel/5.png')] rounded-xl"></div>
        </Slider>
      </div>
    </>
  );
};

export default Carouselcomp;
