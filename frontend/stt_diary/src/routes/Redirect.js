import {useNavigate} from 'react-router-dom';
import { setCookie } from '../utils/cookieManage';

function Redirect() {
    const navigate = useNavigate();

    const loginCode = new URL(window.location).searchParams.get("code");
    if (loginCode) {
        fetch(`${process.env.REACT_APP_API_URL}/api/auth/login?code=${loginCode}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(response => response.json())
        .then(res => {
            setCookie('accessToken', res.accessToken, 2 * 60);
            setCookie('refreshToken', res.refreshToken, 24 * 60 * 7);
            navigate('/');
        })
        .catch(error => {
          alert("관리자에게 문의해주세요.");
        });
    } else {
        alert('잘못된 접근입니다.');
        navigate('/login');
    }
}
export default Redirect;