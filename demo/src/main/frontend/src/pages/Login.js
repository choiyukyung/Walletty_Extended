import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useUserDispatch } from "../context/UserContext";
import Button from "../components/Button";

const URL = process.env.REACT_APP_API_URL;

const Login = () => {
  const [state, setState] = useState({
    id: "",
    password: "",
  });
  const dispatch = useUserDispatch();
  const handleChangeState = (e) => {
    setState({
      ...state,
      [e.target.name]: e.target.value,
    });
  };

  const navigate = useNavigate();

  const onLoginHandler = (e) => {
    console.log(state.id);
    e.preventDefault();
    fetch("/member/login", {
      method: "POST",
      body: JSON.stringify({
        memberId: state.id,
        memberPassword: state.password,
      }),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((result) => result.json())
      .then((result) => {
        if (result.message === "success" && state.id !== "aa") {
          console.log("로그인에 성공하였습니다.");
          localStorage.setItem("memberId", state.id);
          localStorage.setItem("isLoggedIn", "true");
          //localstorage.setItem("token", result.token)
          // local 말고 db에도 데이터 surveyDone 필요
          // 로그인 시 db에서 goal, fixed, 미션 등 가져오기
          navigate("/");
          dispatch({
            type: "LOGIN",
            userId: state.id,
          });
        } else if (state.id === "aa") {
          localStorage.setItem("memberId", state.id);
          localStorage.setItem("isLoggedIn", "true");
          navigate("/survey-feedback");
        } else if (result.message === "successFirst") {
          localStorage.setItem("memberId", state.id);
          localStorage.setItem("isLoggedIn", "true");
          navigate("/survey-feedback");
        } else if (result.message === "fail") {
          alert("아이디나 비밀번호를 확인해 주세요.");
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const onJoinHandler = (e) => {
    e.preventDefault();
    navigate("/join");
  };
  return (
    <div className="Login">
      <div className="login_box">
        <div>
          <input
            name="id"
            type="id"
            value={state.id}
            placeholder="아이디"
            onChange={handleChangeState}
          />
        </div>
        <div>
          <input
            name="password"
            type="password"
            value={state.password}
            placeholder="비밀번호"
            onChange={handleChangeState}
          />
        </div>
        <div className="button_box">
          <div>
            <Button text={"로그인하기"} onClick={onLoginHandler} />
          </div>
          <div className="button_up_text">아이디가 없다면?</div>
          <div>
            <Button text={"회원가입하기"} onClick={onJoinHandler} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;
