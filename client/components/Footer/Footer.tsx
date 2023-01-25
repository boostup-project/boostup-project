interface DevAdds {
  [index: string]: string;
}
const devAdds: DevAdds = {
  김상현: "https://github.com/headring",
  정하늘: "https://github.com/skynotlimit",
  박성훈: "https://github.com/hun0613",
  조경민: "https://github.com/Mozzi327",
  임재민: "https://github.com/LimJaeminZ",
  이규리: "https://github.com/LeeGoh",
  주석영: "https://github.com/Quartz614",
};

const devAlias: string[] = Object.keys(devAdds);

const Footer = () => {
  return (
    <footer className="h-full w-full bg-textColor flex justify-center items-center mt-4">
      <div className="bg-textColor w-full h-28 text-white font-SCDream5 px-1 py-1 flex flex-col justify-between tablet:flex-row tablet:items-center tablet:h-40 desktop:w-3/4 desktop:h-52 desktop:min-w-[1000px]">
        <div className="tablet:flex tablet:flex-col tablet:h-1/2 tablet:justify-around">
          <div className="text-base tablet:text-2xl">(주)코듀온</div>
          <div>
            {devAlias.map((alias, i) => (
              <a
                className="text-xs mr-3 tablet:text-base desktop:hover:underline"
                target="_blank"
                rel="noopener noreferrer"
                key={i}
                href={devAdds[alias]}
              >
                {alias}
              </a>
            ))}
          </div>
          <div className="text-xs tablet:text-base">
            사업자 등록 번호: 012 - 34 - 56789
          </div>
        </div>
        <div className="flex items-center tablet:w-5/12 tablet:h-1/3 tablet:justify-evenly desktop:w-4/12 desktop:justify-end">
          <div className="grow text-base tablet:text-2xl tablet:text-right">
            고객센터
          </div>
          <a
            className="grow text-xs tablet:text-base desktop:hover:underline tablet:text-right"
            target="_blank"
            rel="noopener noreferrer"
            href="https://forms.gle/Gd9A1mYKKR6eqQ1x5"
          >
            구글폼 링크
          </a>
          <div className="grow text-xs tablet:text-base tablet:text-right">
            tel: 02-345-6789
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
