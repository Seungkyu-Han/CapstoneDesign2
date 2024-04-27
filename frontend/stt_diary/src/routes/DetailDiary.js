import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import './DetailDiary.css';
import LoadingModal from '../components/LoadingModal';

function DetailDiary() {
    const { id } = useParams();
    const [diaryData, setDiaryData] = useState(null);
    const [isLoadingModalOpen, setIsLoadingModalOpen] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        fetch(`${process.env.REACT_APP_API_URL}/api/diary?id=${id}`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
        })
        .then((response) => response.json())
        .then((res) => {
            setDiaryData(res);
        })
        .catch(() => {
            alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        });
    },[id]);

    const getDayOfWeek = (date) => { 
        const daysOfWeek = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
        return daysOfWeek[date];
    }

    const handleDeleteDiary = () => {
        fetch(`${process.env.REACT_APP_API_URL}/api/diary?id=${id}`,
        {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(() => {
            alert('일기기 삭제되었습니다.');
            navigate('/');
        })
        .catch(() => {
            alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        });
    }
    
    const handleModifyDiary = () => {
        const modifyBtn = document.querySelector('.modifyBtn');
        const diaryTitle = document.querySelector('.detail-title');
        const diaryContent = document.querySelector('.detail-content');
        if (modifyBtn.innerText === '수정') {
            modifyBtn.innerText = '저장';
            diaryTitle.contentEditable = true;
            diaryContent.readOnly = false;
            diaryTitle.style.border = '2px solid #000';
            diaryContent.style.border = '2px solid #000';
        }
        else if (modifyBtn.innerText === '저장') {
            modifyBtn.innerText = '수정';
            diaryTitle.contentEditable = false;
            diaryContent.readOnly = true;
            diaryTitle.style.border = '1px solid #000';
            diaryContent.style.border = '1px solid #000';
            const today = new Date();
            const todayDate = today.toISOString().split('T')[0];
            fetch(`${process.env.REACT_APP_API_URL}/api/diary`,
                {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        id : id,
                        title: diaryTitle.innerText,
                        content: diaryContent.value,
                        date: todayDate,
                })
            })
            .then(() => {
                const sentimentWrapper = document.querySelector('.sentiment-wrapper');
                sentimentWrapper.remove();
                const chattingWrapper = document.querySelector('.chatting-wrapper');
                chattingWrapper.remove();
                setIsLoadingModalOpen(true);
            })
            .catch(() => {
                alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
            });
        }
    }

    if (diaryData) {
        const dateArray = diaryData.date.split('-');
        const weekDay = new Date(diaryData.date).getDay();
        const formattedDate = `${dateArray[0]}년 ${dateArray[1]}월 ${dateArray[2]}일. ${getDayOfWeek(weekDay)}`;
        return (
            <div className='detail-diary-container'>
                <div className="diary-content-wrapper">
                    <div className='diary-header-wrapper'>
                        <div className="detail-date-wrapper">
                            <img src = {require('../assets/calendar-icon.png')} alt=""/>
                            <p className='detail-date'>{formattedDate} </p>
                        </div>
                        <div className='detail-button-wrapper'>
                            <button className='deleteBtn' onClick={handleDeleteDiary}>삭제</button>
                            <button className='modifyBtn' onClick={handleModifyDiary}>수정</button>
                        </div>
                    </div>
                    <div className="detail-title" contentEditable="false" suppressContentEditableWarning={true}>{diaryData.title}</div>
                    <textarea className="detail-content" defaultValue={diaryData.content} readOnly/>
                </div>
                <div className="sentiment-wrapper">
                    <div className='sentiment-header'>
                        <span className='result1'>감정 분석 결과: </span>
                        <span className='result2'>긍정적</span>
                    </div>
                    <div className='sentiment-content'>... 감정분석 내용 ...</div>
                </div>
                <div className="chatting-wrapper">
                    <div className='answer-wrapper'>안녕하세요! 심리상담 챗봇입니다.<br/>어떤 문제로 도와드릴까요?</div>
                    <div className='question-wrapper'>요즘 너무 스트레스 받아요. 일상 생활에 집중이 안돼요</div>
                    <div className='answer-wrapper'>그게 어떤 일이세요? 스트레스의 원인이 무엇인지 알려주시겠어요?</div>
                    <div className='question-wrapper'>업무와 학업의 부담이 너무 커서요. 시간이 부족하고 갑작스러운 일정 때문에 힘들어요.</div>
                    <div className='answer-wrapper'>이해합니다. 스트레스는 우리에게 많은 영향을 미칠 수 있습니다. 하루에 일정한 휴식 시간을 가져보는 것이 어떨까요? 스트레스 관리를 위해 규칙적인 운동이나 심호흡도 도움이 될 수 있습니다.</div>
                    <div className='question-wrapper'>네, 그런 생각을 해보지 않았어요. 감사합니다.</div>
                </div>
                {isLoadingModalOpen && <LoadingModal setIsLoadingModalOpen={setIsLoadingModalOpen} />}
            </div>
        )
    }else {
        return <p className='noDataMessage'>해당 일기를 조회 중입니다....</p>;
    }

}

export default DetailDiary;
