import { useEffect } from "react";

interface Props {
  children: React.ReactNode;
  onClick: (e: React.MouseEvent<HTMLDivElement>) => void;
}

/** onClick에는 Modal을 해제하는 함수 할당 **/
const ModalBackDrop = ({ children, onClick }: Props) => {
  useEffect(() => {
    document.body.style.cssText = `
    overflow: hidden;
    width: 100%;`;
    return () => {
      document.body.style.cssText = "overflow: unset;";
    };
  }, []);

  return (
    <div
      className="fixed z-[999] top-0 left-0 bottom-0 right-0 bg-modalBgColor grid place-items-center"
      onClick={onClick}
    >
      {children}
    </div>
  );
};
export default ModalBackDrop;
