import Button from "../components/Button";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const Survey_goalmoney = () => {
  const navigate = useNavigate();
  const id = localStorage.getItem("memberId");
  const fixedList = JSON.parse(localStorage.getItem("fixed_entry"));
  const goalList = JSON.parse(localStorage.getItem("goal_entry"));
  const [goalmoneyList, setGoalmoneyList] = useState({
    goalmoney1: "10000",
    goalmoney2: "10000",
    goalmoney3: "10000",
  });
  const [leastGoalMoney, setLeastGoalMoney] = useState("");
  const [FilteredLeastGoalMoney, setFilteredLeastGoalMoney] = useState("");
  const handleChangeState = (e) => {
    setGoalmoneyList({
      ...goalmoneyList,
      [e.target.name]: e.target.value,
    });
  };
  const onSubmitHandler = (e) => {
    e.preventDefault();
    if (goalmoneyList.goalmoney1 > leastGoalMoney.entry1totalAmount90) {
      alert(`${goalList[0].name} 항목 금액을 조건에 맞게 다시 설정해 주세요.`);
    } else if (goalmoneyList.goalmoney2 > leastGoalMoney.entry2totalAmount90) {
      alert(`${goalList[1].name} 항목 금액을 조건에 맞게 다시 설정해 주세요.`);
    } else if (goalmoneyList.goalmoney3 > leastGoalMoney.entry3totalAmount90) {
      alert(`${goalList[2].name} 항목 금액을 조건에 맞게 다시 설정해 주세요.`);
    } else if (
      !goalmoneyList.goalmoney1 ||
      !goalmoneyList.goalmoney2 ||
      !goalmoneyList.goalmoney3
    ) {
      alert("모든 항목을 입력해 주세요.");
    } else {
      fetch("/survey", {
        method: "POST",
        body: JSON.stringify({
          //local에서 꺼내쓰기
          surveyId: id,
          fixedEntry: fixedList.map((row) => row.value).toString(),
          goalEntry1: goalList[0].value,
          goalMoney1: Number(goalmoneyList.goalmoney1),
          goalEntry2: goalList[1].value,
          goalMoney2: Number(goalmoneyList.goalmoney2),
          goalEntry3: goalList[2].value,
          goalMoney3: Number(goalmoneyList.goalmoney3),
        }),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          Swal.fire({
            html: `<div class="text_alert_box"><div>목표 설정이 완료되었습니다!</div></div>`,
            customClass: "text-alert",
            showConfirmButton: false,
            position: "top",
            timer: 1000,
          }).then(function (result) {
            setTimeout(() => {
              navigate("/");
            }, 500);
          });
        })
        .catch((error) => console.log(error));
    }
  };

  useEffect(() => {
    fetch("/survey/limit", {
      method: "POST",
      body: JSON.stringify({
        surveyId: id,
        fixedEntry: fixedList.map((row) => row.value).toString(),
        goalEntry1: goalList[0].value,
        goalEntry2: goalList[1].value,
        goalEntry3: goalList[2].value,
      }),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((result) => {
        setLeastGoalMoney(result);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  useEffect(() => {
    console.log(leastGoalMoney);
    console.log(goalList);
  }, [leastGoalMoney]);

  return (
    <div>
      <div className="survey_qs">
        <div className="survey_q_box">
          <div className="survey_q_text">
            절약하고 싶은 항목별로 목표 금액을 설정해 주세요.<br></br> 목표
            금액은 해당 항목 소비량의 90% 이하여야 합니다. (10% 이상 절약해야
            함)
          </div>
          <div className="goalmoney_list_box">
            <div className="goalmoney_box_element">
              {`${goalList[0].name} 항목의 절약 목표 금액을 설정해 주세요:`}
              <input
                name={`goalmoney1`}
                type="text"
                placeholder={`${leastGoalMoney.entry1totalAmount90} 이하`}
                onChange={handleChangeState}
              />
            </div>
            <div className="goalmoney_box_element">
              {`${goalList[1].name} 항목의 절약 목표 금액을 설정해 주세요:`}
              <input
                name={`goalmoney2`}
                type="text"
                placeholder={`${leastGoalMoney.entry2totalAmount90} 이하`}
                onChange={handleChangeState}
              />
            </div>
            <div className="goalmoney_box_element">
              {`${goalList[2].name} 항목의 절약 목표 금액을 설정해 주세요:`}
              <input
                name={`goalmoney3`}
                type="text"
                placeholder={`${leastGoalMoney.entry3totalAmount90} 이하`}
                onChange={handleChangeState}
              />
            </div>
          </div>
        </div>
        <Button text={"설정 완료"} onClick={onSubmitHandler} type={"survey"} />
      </div>
    </div>
  );
};

export default Survey_goalmoney;
