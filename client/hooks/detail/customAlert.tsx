import Swal from "sweetalert2";

export const deleteAlert = (text: any, text2: any) => {
  return Swal.fire({
    text: text,
    icon: "question",
    showCancelButton: true,
    confirmButtonText: "확인",
    cancelButtonText: "취소",
    buttonsStyling: false,
    customClass: {
      container: "customAlert-container",
      popup: "customAlert-container",
      title: "customAlert-title",
      confirmButton: "customAlert-button",
      cancelButton: "customAlert-button2",
    },
  }).then(result => {
    if (result.isConfirmed) {
      return Swal.fire({
        text: text2,
        icon: "success",
        confirmButtonText: "확인",
        buttonsStyling: false,
        customClass: {
          container: "customAlert-container",
          popup: "customAlert-container",
          title: "customAlert-title",
          confirmButton: "customAlert-button",
        },
      });
    }
  });
};
