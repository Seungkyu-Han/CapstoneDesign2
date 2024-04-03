
import NavigationItem from "./NavigationItem";

function NavigationMenu({ activePage, navigate, toggleDropdown }) {
    return (
      <>
        <NavigationItem path="" activePage={activePage} onClick={() => {navigate('/'); toggleDropdown();}} title="나의 일기장" />
        <NavigationItem path="analysis" activePage={activePage} onClick={() => {navigate('/analysis'); toggleDropdown();}} title="나의 감정분석" />
        <NavigationItem path="login" activePage={activePage} onClick={() => {navigate('/login'); toggleDropdown();}} title="로그아웃" />
      </>
    );
}

export default NavigationMenu;