interface Props {
  width?: string;
  heigth?: string;
}

export const IconChat = ({ width, heigth }: Props) => {
  return (
    <svg
      width={width}
      height={heigth}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M12 0C18.6 0 24 4.77333 24 10.6667C24 16.56 18.6 21.3333 12 21.3333C10.512 21.3333 9.084 21.0933 7.764 20.6667C4.26 24 0 24 0 24C2.796 20.8933 3.24 18.8 3.3 18C1.26 16.0933 0 13.5067 0 10.6667C0 4.77333 5.4 0 12 0Z"
        fill="black"
      />
    </svg>
  );
};
