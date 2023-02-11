import ModalBackDrop from "components/reuse/container/ModalBackDrop";
import Image from "next/image";

interface Props {
  modalImageClose: () => void;
  imagesToShow: string;
}

const DetailImageModal = ({ modalImageClose, imagesToShow }: Props) => {
  return (
    <ModalBackDrop onClick={modalImageClose}>
      <div className="w-fit h-fit">
        <Image src={imagesToShow} alt="imagesPop" width={500} height={500} />
      </div>
    </ModalBackDrop>
  );
};

export default DetailImageModal;
