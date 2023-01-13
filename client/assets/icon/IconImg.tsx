interface Props {
  width?: string;
  heigth?: string;
  fill?: string;
}
/** OPTION: color값에 hex 코드, width 및 height은 px값 입력 **/
export const IconImg = ({ width, heigth, fill }: Props) => {
  return (
    <svg
      width={width}
      height={heigth}
      xmlns="http://www.w3.org/2000/svg"
      className="w-6 h-6 text-gray-400 group-hover:text-gray-600"
      viewBox="0 0 20 20"
    >
      <path
        d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z"
        fill={fill ? fill : "black"}
      />
    </svg>
  );
};
