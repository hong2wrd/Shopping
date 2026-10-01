import Auth from "../components/Auth.tsx";
import useToken from "../hooks/useToken.tsx";

const Login = () => {
    useToken();
  return (
    <div className="login">
      <Auth/>
    </div>
  )
}

export default Login;