import { AxiosResponse } from "axios";
import { useEffect } from "react";
import { useState } from "react";
import dynamic from "next/dynamic";
import "react-quill/dist/quill.snow.css";
const ReactQuill = dynamic(() => import("react-quill"), {
  ssr: false,
});

interface Props {
  curData: AxiosResponse<any, any> | undefined;
}

const dummy =
  "writing. \n ## Title2 \n title none asdlajsld asldkjalskdj laskdjlaskjd laskdj laksjdlaksj ldkajsdl \n adasdas \n asdkajlsdkj \n aksdjhaksjdhaksj \n akjdhaksjdhaksjhd \n asdjkhaskdjhak \n asdjhaksdjhaksjdh \nkajsdhaksjdh \nqw9iepoqwiepqwoiep \n qweoiqpwoeipqwoei";

const DetailCurriculum = ({ curData }: Props) => {
  const [textData, setTextData] = useState<string>("");
  const [quillText, setQuillText] = useState<string>("");
  console.log("text", textData);
  useEffect(() => {
    if (curData) {
      setTextData(curData.data.curriculum);
    }
  }, [curData]);
  const onChange = (e: string) => {
    setQuillText(e);
  };
  return (
    <div className="w-full h-full p-6 text-base">
      {/* <div className="font-SCDream5">진행방식</div> */}
      {/* <div>{textData}</div> */}
      <ReactQuill onChange={e => onChange(e)} />
      <div
        className="quillBody"
        dangerouslySetInnerHTML={{ __html: quillText }}
      />
    </div>
  );
};

export default DetailCurriculum;

// <div className="detailCurriculum w-full h-fit">
//   <div data-color-mode="light"></div>
// </div>
// const MDEditor = dynamic(() => import("@uiw/react-md-editor"), {
//   ssr: false,
// });
// {
//   /* <MDEditor
//             preview="preview"
//             value={dummy}
//             fullscreen={false}
//             // onHeightChange={100%}
//             // onHeightChange={}
//             // height={`100%`}
//           /> */
// }
