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
    type: "div",
    image: <IconPlus />,
  },
  {
    type: "Link",
    image: <IconChat />,
    link: "/",
  },
  {
    type: "div",
    image: <IconCode />,
  },
];

const Navbar = () => {
  return (
    <div className="bg-white w-full h-16 flex justify-center items-center sticky bottom-0  desktop:hidden">
      {navContents.map((content, i) => (
        <div className="w-1/5 flex justify-center items-center">
          {content.type === "Link" ? (
            <div className="w-6">
              <Link href={content.link!}>{content.image}</Link>
            </div>
          ) : (
            <div className="w-6">
              <div className="bg-pointColor">{content.image}</div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};
export default Navbar;
