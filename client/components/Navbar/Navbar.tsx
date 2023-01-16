import {
  IconChat,
  IconCode,
  IconHome,
  IconProfile,
  IconPlus,
} from "assets/icon/";
import Link from "next/link";

const navContents = [
  {
    type: "Link",
    image: <IconHome />,
    link: "/",
  },
  {
    type: "Link",
    image: <IconProfile />,
    link: "/",
  },
  {
    type: "Write",
    image: <IconPlus />,
  },
  {
    type: "Link",
    image: <IconChat />,
    link: "/",
  },
  {
    type: "Filter",
    image: <IconCode />,
  },
];

const Navbar = () => {
  return (
    <div className="bg-white w-full h-16 flex justify-center items-center sticky bottom-0  desktop:hidden">
      {navContents.map((content, i) => (
        <div className="w-1/5 flex justify-center items-center">
          {content.type === "Link" ? (
            <div key={i} className="w-7">
              <Link href={content.link!}>{content.image}</Link>
            </div>
          ) : content.type === "Write" ? (
            <div
              key={i}
              className="w-12 h-12 rounded-3xl bg-pointColor flex justify-center items-center"
            >
              <div className="w-5">{content.image}</div>
            </div>
          ) : (
            <div key={i} className="w-7">
              <div>{content.image}</div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};
export default Navbar;
