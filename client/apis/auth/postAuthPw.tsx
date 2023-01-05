import axios from "axios";

interface Props {
    name : string,
    email : string
}

const postAuthPw = async({name, email}:Props) => {
    const data = {
        name : name,
        email : email
    }

    console.log(data);
    return await axios.post(`/auth/find/password`, data, {
        baseURL: process.env.NEXT_PUBLIC_API_URL,
    })
}

export default postAuthPw;