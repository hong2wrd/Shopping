import './Auth.css';
import {useRef, useState} from "react";
import API from "../hooks/API.tsx";
import {setToken} from "../hooks/useToken.tsx";
import {useNavigate} from "react-router-dom";

const Auth = () => {
    const nav = useNavigate();
    const [input, setInput] = useState({
        id: '',
        password: '',
    });
    const [typePassword, setTypePassword] = useState(true);
    const [span, setSpan] = useState({
        id: '',
        password: '',
        login : ''
    });
    const inputRef = useRef<Record<string, HTMLInputElement | null>>({});
    const loginRef = useRef(false);

    const onChangeInput = (e:any) => {
        setInput({...input, [e.target.name]: e.target.value});
    }

    const onClickDelButton = (name:string) => {
        setInput({...input, [name]: ""});
        inputRef.current[name]?.focus();
    }

    const setRef = (el:HTMLInputElement | null) => {
        if (!el) return;
        inputRef.current[el.name] = el;
    }

    /**
     * 유효성 검사
     */
    function inputValidation(): boolean {
        if(input.id === "" || input.password === ""){
            return false;
        }

        return true;
    }

    const onClickLogin = async () => {

        if(loginRef.current) return;

        if(!inputValidation()) return;

        try {
            loginRef.current = true;
            const response = await API.post("/login", input);

            const data = response.data;

            setInput({
                id : "",
                password : ""
            });

            if(data.status !== 200) {
                span.login = data.message;
                return;
            }

            const token = response.headers["Authorization"];
            setToken(typeof token === "string" ? token : "");

            nav("/", {replace : true});
        } catch (error) {
            console.error(error);
        } finally {
            loginRef.current = false;
        }
    }

    return (
        <div className="auth">
            <div className="auth__form">
                <label htmlFor="id">아이디</label>
                <input id={"id"}
                       name={"id"}
                       type={"text"}
                       aria-label="아이디"
                       value={input.id}
                       ref={setRef}
                       onChange={onChangeInput}
                />
                <button onClick={() => onClickDelButton("id")}><span>삭제</span></button>
            </div>
            <div>
                <input type={typePassword ? "password" : "text"}
                       name={"password"}
                       value={input.password}
                       ref={setRef}
                       onChange={onChangeInput}
                />
                <button onClick={() => setTypePassword(!typePassword)}><span>비밀번호 표시</span></button>
                <button onClick={() => onClickDelButton("password")}><span>삭제</span></button>
            </div>
            <div>
                <span></span>
            </div>
            <div>
                <button onClick={onClickLogin}>로그인</button>
            </div>

        </div>
      )
}

export default Auth;