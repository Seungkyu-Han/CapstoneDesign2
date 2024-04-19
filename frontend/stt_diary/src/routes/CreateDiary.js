import React from 'react';
import { useNavigate } from 'react-router-dom';
import './CreateDiary.css';


function CreateDiary() {
    const currentDate = new Date();
    const navigate = useNavigate();

    const getDayOfWeek = (currentDate) => { 
        const daysOfWeek = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
        return daysOfWeek[currentDate.getDay()];
    }
    const formattedDate = `${currentDate.getFullYear()}년 ${currentDate.getMonth() + 1}월 ${currentDate.getDate()}일 . ${getDayOfWeek(currentDate)}`;

    const handleCancelButton = () => {
        navigate('/')
    }
    
    const handleSaveButton = () => {
        const title = document.querySelector('.diaryTitle').value;
        const content = document.querySelector('.diaryContent').value;
        const date = currentDate.toISOString().split('T')[0];
        if (title === '') {
            alert('제목을 입력해주세요.');
            return;
        }
        else if (content === '') {
            alert('내용을 입력해주세요.');
            return;
        }
        fetch(`${process.env.REACT_APP_API_URL}/api/diary`,
            {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    userId : 1,
                    title: title,
                    content: content,
                    date: date,
            })
        })
        .then(() => {
            alert('일기가 저장되었습니다.');
            navigate('/');
        })
        .catch((error) => console.log(error));
    };

    return (
        <div className='create-diary-wrapper'>
        <span className="diaryDate">{formattedDate}</span>
        <input className="diaryTitle" type="text" placeholder="제목을 입력하세요." />
        <textarea className="diaryContent" placeholder="일기 내용을 입력하세요."></textarea>
        <div className="buttonContainer">
            <button className="cancelButton" onClick={handleCancelButton}>취소</button>
            <div className="recordButton">
                <img src={require('../assets/record-icon.png')} alt=""/>
            </div>
            <button className="saveButton" onClick={handleSaveButton}>저장</button>
        </div>
        </div>
    );
}

export default CreateDiary;