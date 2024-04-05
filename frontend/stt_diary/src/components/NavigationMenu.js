
import React from 'react';
import { useNavigate } from 'react-router-dom';
import NavigationItem from "./NavigationItem";

function NavigationMenu({toggleDropdown}) {
  const navigate = useNavigate();
  
  return (
    <>
      <NavigationItem path="" title="나의 일기장" navigate={navigate} toggleDropdown={toggleDropdown}/>
      <NavigationItem path="analysis" title="나의 감정분석" navigate={navigate} toggleDropdown={toggleDropdown} />
      <NavigationItem path="login" title="로그아웃" navigate={navigate} toggleDropdown={toggleDropdown} />
    </>
  );
}

export default NavigationMenu;
