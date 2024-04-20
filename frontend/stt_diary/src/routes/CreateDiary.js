import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './CreateDiary.css';
import RecordModal from '../components/RecordModal';
import LoadingModal from '../components/LoadingModal';


function CreateDiary() {
    const currentDate = new Date();
    const [isRecordModalOpen, setIsRecordModalOpen] = useState(false);
    const [isLoadingModalOpen, setIsLoadingModalOpen] = useState(false);
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
        const title = document.querySelector('.diary-create-title').value;
        const content = document.querySelector('.diary-create-content').value;
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
            setIsLoadingModalOpen(true);
        })
        .catch(() => {
            alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        });
    };

    const handleRecordButton = () => {
        setIsRecordModalOpen(true);
    };

    return (
        <div className='create-diary-wrapper'>
            <span className="diary-create-date">{formattedDate}</span>
            <input className="diary-create-title" type="text" placeholder="제목을 입력하세요." />
            <textarea className="diary-create-content " placeholder="일기 내용을 입력하세요."></textarea>
            <div className="button-container">
                <button className="cancel-button" onClick={handleCancelButton}>취소</button>
                <div className="record-button" onClick={handleRecordButton}>
                    <img src={require('../assets/record-icon.png')} alt=""/>
                </div>
                <button className="save-button" onClick={handleSaveButton}>저장</button>
            </div>
            {isRecordModalOpen && <RecordModal setIsRecordModalOpen={setIsRecordModalOpen} />}
            {isLoadingModalOpen && <LoadingModal setIsLoadingModalOpen={setIsLoadingModalOpen} />}
        </div>
    );
}

export default CreateDiary;