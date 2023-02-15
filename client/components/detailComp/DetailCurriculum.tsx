import { AxiosResponse } from "axios";
import { useEffect } from "react";
import { useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import DetailCurModi from "./DetailCurModi";

interface Props {
  curData: AxiosResponse<any, any> | undefined;
  editable: boolean | undefined;
}

const DetailCurriculum = ({ curData, editable }: Props) => {
  const [displayText, setDisplayText] = useState<string>("");
  const [textData, setTextData] = useState<string>("");
  const [isEdit, setIsEdit] = useState(false);
  useEffect(() => {
    if (curData) {
      setTextData(curData.data.curriculum);
      setDisplayText(curData.data.curriculum);
    }
  }, [curData]);
  const modalOpen = () => {
    setIsEdit(prev => !prev);
  };

  return (
    <>
      {isEdit && (
        <DetailCurModi
          textData={textData}
          setTextData={setTextData}
          modalOpen={modalOpen}
        />
      )}
      <div className="w-full h-full p-6 text-base">
        <div className="flex justify-between mb-3">
          {editable && (
            <div
              onClick={modalOpen}
              className="text-pointColor font-SCDream3 cursor-pointer hover:underline"
            >
              edit
            </div>
          )}
        </div>
        <div className="bg-markBgColor rounded-xl p-4">
          <div className="prose">
            <ReactMarkdown remarkPlugins={[remarkGfm]}>
              {displayText}
            </ReactMarkdown>
          </div>
        </div>
      </div>
    </>
  );
};

export default DetailCurriculum;
