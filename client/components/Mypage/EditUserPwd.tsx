import CreateModalContainer from "components/reuse/container/CreateModalContainer";
import ModalBackDrop from "components/reuse/container/ModalBackDrop";

interface Props {
  editPWd: () => void;
}

const EditUserPwd = ({ editPWd }: Props) => {
  return (
    <ModalBackDrop onClick={editPWd}>
      <CreateModalContainer>
        <div>hi</div>
      </CreateModalContainer>
    </ModalBackDrop>
  );
};

export default EditUserPwd;
