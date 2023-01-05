import { useMutation } from "react-query"
import postAuthPw from "apis/auth/postAuthPw"

const usePostAuthPw = () => {
    return useMutation(postAuthPw, {
        onSuccess : () => {
            alert("요청성공!")
        },
        onError: (error) => {
            console.log(error)
            alert("요청실패!")
        }
    });
}

export default usePostAuthPw;