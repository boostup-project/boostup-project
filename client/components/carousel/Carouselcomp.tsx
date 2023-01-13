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
  };
  return (
    <>
      <div className="w-full desktop:w-3/4 desktop:min-w-[1000px] h-[250px]  mt-5 rounded-xl">
        {/* <Image src={"/images/temp_image.png"}  width={250} height={100} alt="photo1" priority={true} /> */}
        <Slider {...settings} className="w-full h-full">
          <div className="flex items-center justify-center w-full h-[250px] bg-slate-400 rounded-xl"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-slate-500 rounded-xl"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-slate-600 rounded-xl"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-slate-700 rounded-xl"></div>
          <div className="w-full h-[250px] flex flex-col justify-center items-center bg-slate-800 rounded-xl"></div>
        </Slider>
      </div>
    </>
  );
};

export default Carouselcomp;
