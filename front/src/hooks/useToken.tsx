import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

const Authorization = "Authorization";

/**
 * local Storage에 토큰 저장
 * @param token JWT Token
 */
export const setToken = (token:string)=> {
    localStorage.setItem(Authorization, token);
}

/**
 * 토큰 유무 확인
 */
export const isToken = () : boolean | false => {
    return localStorage.getItem(Authorization)?.startsWith("Bearer ") ?? false;
}

const useToken = () => {
    const nav = useNavigate();

    useEffect(() => {
        if(isToken()) {
            nav('/', {replace: true})
        }
    }, []);
}

export default useToken;