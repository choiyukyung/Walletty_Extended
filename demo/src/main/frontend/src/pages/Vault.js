import { useEffect, useState } from "react";
import MyHeader from "../components/MyHeader";
import VaultList from "../components/VaultList";
import { useNavigate } from "react-router-dom";
import MissionList from "../components/MissionList";

const Vault = () => {
  const id = localStorage.getItem("memberId");
  const navigate = useNavigate();
  const [missionList, setMissionList] = useState([]);
  const [vaultList, setVaultList] = useState([]);
  const [data, setData] = useState([]);
  useEffect(() => {
    if (localStorage.getItem("isLoggedIn") === "true") {
      fetch("/safe", {
        method: "GET",
        body: JSON.stringify({
          memberId: id,
        }),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((result) => result.json())
        .then((result) => {
          setVaultList(result);
          console.log(result);
        })
        .catch((err) => {
          console.log(err);
        });
      fetch("/chat-gpt/mission", {
        method: "POST",
        body: JSON.stringify({
          memberId: id,
        }),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((result) => result.json())
        .then((result) => {
          setMissionList(result);
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      navigate("/login");
    }
  }, []);
  useEffect(() => {
    const map = new Map();
    missionList.forEach((item) => map.set(item.missionId, item));
    vaultList.forEach((item) =>
      map.set(item.missionId, { ...map.get(item.missionId), ...item })
    );
    const missionVaultList = Array.from(map.values());
    const newList = missionVaultList.map(
      ({ startDate, endDate, ...rest }) => rest
    );
    const newnewList = newList.filter((it) => it.missionSen);
    setData(newnewList);
    console.log(data);
  }, [vaultList, missionList]);

  return (
    <div>
      <MyHeader nowpage={"vault"} />
      <VaultList totalVaultList={data} />
    </div>
  );
};

export default Vault;
