import CreateModalContainer from "components/reuse/container/CreateModalContainer";
import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import Image from "next/image";

interface Props {
  modalImageClose: () => void;
  imagesToShow: string;
}

const DetailImageModal = ({ modalImageClose, imagesToShow }: Props) => {
  return (
    <ModalBackDrop onClick={modalImageClose}>
      {/* <CreateModalContainer> */}
      <div className="w-fit h-fit">
        <Image src={imagesToShow} alt="imagesPop" width={300} height={300} />
      </div>
      {/* </CreateModalContainer> */}
    </ModalBackDrop>
  );
};

export default DetailImageModal;
