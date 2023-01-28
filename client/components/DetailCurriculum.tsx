import { AxiosResponse } from "axios";
import { useEffect } from "react";
import { useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";

interface Props {
  curData: AxiosResponse<any, any> | undefined;
}

const dummy = `## Title2 
asdasd asdasdas
asdasd asdas
1. asdasd
- Foo
- Bar
  * Baz
    + Nope?
  * Hai?
`;

const DetailCurriculum = ({ curData }: Props) => {
  const [textData, setTextData] = useState<string>("");
  const [isEdit, setIsEdit] = useState(false);
  useEffect(() => {
    if (curData) {
      setTextData(curData.data.curriculum);
    }
  }, [curData]);
  const modalOpen = () => {
    setIsEdit(prev => !prev);
  };
  return (
    <>
      {isEdit && <div>hi</div>}
      <div className="w-full h-full p-6 text-base">
        <div className="flex justify-between mb-3">
          <div className="font-SCDream5">진행방식</div>
          <div
            onClick={modalOpen}
            className="text-pointColor font-SCDream3 cursor-pointer hover:underline"
          >
            edit
          </div>
        </div>
        <div className="bg-markBgColor rounded-xl p-4">
          <div className="prose">
            <ReactMarkdown remarkPlugins={[remarkGfm]}>{dummy}</ReactMarkdown>
          </div>
        </div>
      </div>
    </>
  );
};

export default DetailCurriculum;
