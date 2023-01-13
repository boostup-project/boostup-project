interface Props {
  width?: string;
  heigth?: string;
}

export const IconHome = ({ width, heigth }: Props) => {
  return (
    <svg
      width={width}
      height={heigth}
      viewBox="0 0 25 25"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M25 14.4157L12.5 4.7126L0 14.4157V10.4595L12.5 0.756348L25 10.4595V14.4157ZM21.875 14.0626V23.4376H15.625V17.1876H9.375V23.4376H3.125V14.0626L12.5 7.03135L21.875 14.0626Z"
        fill="#424242"
      />
    </svg>
  );
};
