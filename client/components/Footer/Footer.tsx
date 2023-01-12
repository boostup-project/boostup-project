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
    <footer className="bg-textColor w-full h-28 text-white font-SCDream5">
      <div>
        <div className="text-xl">코듀온</div>
        <div>
          {devAlias.map((alias, i) => (
            <a
              className="text-base"
              target="_blank"
              rel="noopener noreferrer"
              key={i}
              href={devAdds[alias]}
            >
              {alias}
            </a>
          ))}
        </div>
        <div>사업자 등록 번호</div>
      </div>
      <div>
        <div>고객센터</div>
      </div>
    </footer>
  );
};

export default Footer;
