import './Auth.css';
import {useRef, useState} from "react";
import API from "../hooks/API.tsx";
import {setToken} from "../hooks/useToken.tsx";
import {useNavigate} from "react-router-dom";


const Auth = () => {
    type User = {
        id: string;
        password: string;
    }

    const nav = useNavigate();
    const [user, setUser] = useState<User>({
        id: "",
        password: ""
    });
    const [typePassword, setTypePassword] = useState(true);
    const [span, setSpan] = useState({
        id: '',
        password: '',
        login : ''
    });
    const inputRef = useRef<Record<string, HTMLInputElement | null>>({});

    /** 로그인 시도 유무 */
    const loginRef = useRef(false);

    const onChangeInput = (e:any) => {
        setUser({...user, [e.target.name]: e.target.value});
    }

    const onClickDelButton = (name:string) => {
        setUser({...user, [name]: ""});
        inputRef.current[name]?.focus();
    }

    const setRef = (el:HTMLInputElement | null) => {
        if (!el) return;
        inputRef.current[el.name] = el;
    }

    /**
     * 유효성 검사
     */
    function inputValidation(user:User | null): boolean {
        if(user?.id === "" || user?.password === "") {
            return false;
        }

        // TODO 유효성 검사 추가

        return true;
    }

    const onClickLogin = async () => {

        if(loginRef.current) return;

        if(!inputValidation(user)) return;

        try {
            loginRef.current = true;
            const response = await API.post("/login", user);

            const data = response.data;

            setUser({
                id : "",
                password : ""
            });

            if(data.status !== 200) {
                setSpan({...span, login: data.message});
                return;
            }

            const token:string = response.headers["Authorization"] ?? "";

            if(token === "") {
                setSpan({...span, login: "로그인에 실패했습니다. 잠시 후 다시 시도 바랍니다."});
                return;
            }

            setToken(token);

            nav("/", {replace : true});
        } catch (error) {
            console.error(error);
        } finally {
            loginRef.current = false;
        }
    }

    return (
        <div className="auth">
            <div className="auth_id">
                <label id={"id_label"} htmlFor="id">아이디</label>
                <input id={"id"}
                       name={"id"}
                       type={"text"}
                       aria-label="아이디"
                       value={user.id}
                       ref={setRef}
                       onChange={onChangeInput}
                />
                <button type="button"
                        className="auth_icon_button auth_del_button"
                        onClick={() => onClickDelButton("id")}>
                    <span className="auth_blind">아이디 지우기</span>
                </button>
            </div>
            <div className="auth_password">
                <label id={"password_label"} htmlFor="password">비밀번호</label>
                <input id={"password"}
                       type={typePassword ? "password" : "text"}
                       name={"password"}
                       value={user.password}
                       ref={setRef}
                       onChange={onChangeInput}
                />
                <button type="button"
                        className={`auth_icon_button auth_password_button${typePassword ? '' : ' is_visible'}`}
                        onClick={() => setTypePassword(!typePassword)}>
                    <span className="auth_blind">{typePassword ? '비밀번호 표시' : '비밀번호 숨기기'}</span>
                </button>
                <button type="button"
                        className="auth_icon_button auth_del_button"
                        onClick={() => onClickDelButton("password")}>
                    <span className="auth_blind">비밀번호 지우기</span>
                </button>
            </div>
            <div>
                <span>{span.login}</span>
            </div>
            <div>
                <button onClick={onClickLogin}>로그인</button>
            </div>

        </div>
      )
}

export default Auth;
