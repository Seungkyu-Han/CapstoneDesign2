import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import './DetailDiary.css';
import { getCookie } from '../utils/cookieManage';

function DetailDiary() {
    const { id } = useParams();
    const [diaryData, setDiaryData] = useState(null);
    const [diaryResult, setDiaryResult] = useState({ result: null, resultAnalysis: null });
    const [chattings, setChattings] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        //일기 정보
        fetch(`${process.env.REACT_APP_API_URL}/api/diary?id=${id}`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + getCookie('accessToken'),
                },
        })
        .then((response) => {
            if (response.status === 200) {
              return response.json();
            } 
            
        })
        .then((res) => {
            setDiaryData(res);
        })
        .catch(() => {
            alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
        });

        //감정 분석 결과
        fetch(`${process.env.REACT_APP_API_URL}/api/feeling?diaryId=${id}`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + getCookie('accessToken'),
                },
            })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                    navigate('/');
                }
            })
            .then((res) => {
                setDiaryResult(prevResult => ({ ...prevResult, result: res.emotion }));
            })
            .catch(error => {
                alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                navigate('/');
            });

        //감정분석 내용
        fetch(`${process.env.REACT_APP_API_URL}/api/chatGPT/diary?diaryId=${id}`,
            {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + getCookie('accessToken'),
                },
            })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                    navigate('/');
                }
            })
            .then((res) => {
                setDiaryResult(prevResult => ({ ...prevResult, resultAnalysis: res.content }));
            })
            .catch(error => {
                alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                navigate('/');
            });

        //상담 내용
        fetch(`${process.env.REACT_APP_API_URL}/api/chatGPT/consult?diaryId=${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + getCookie('accessToken'),
            },
        })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                    navigate('/');
                }
            })
            .then((res) => {
                setChattings(res);
            })
            .catch(error => {
                alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                navigate('/');
            });

    }, [id]);

    const getDayOfWeek = (date) => {
        const daysOfWeek = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
        return daysOfWeek[date];
    }

    const handleDeleteDiary = () => {
        const confirm = window.confirm('일기 삭제 시 상담 내용 및 감정 분석 결과도 함께 삭제됩니다. \n정말 삭제하시겠습니까?');
        if (!confirm) return;
        fetch(`${process.env.REACT_APP_API_URL}/api/diary?id=${id}`,
            {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + getCookie('accessToken'),
                },
            })
            .then((res) => {
                if (res.status === 200) {
                    deleteChattings();
                    alert('일기기 삭제되었습니다.');
                    navigate('/');
                }
                else {
                    throw new Error();
                }
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
                        'Authorization': 'Bearer ' + getCookie('accessToken'),
                    },
                    body: JSON.stringify({
                        id : id,
                        title: diaryTitle.innerText.trim(),
                        content: diaryContent.value,
                        date: todayDate,
                })
            })
            .then((res) => {
                if (res.status === 200) {
                        const sentimentWrapper = document.querySelector('.sentiment-wrapper');
                        sentimentWrapper.remove();
                        deleteChattings();
                        navigate(`/result/modify/${id}`);
                }
                else {
                    throw new Error();
                }    
            })
            .catch(() => {
                alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
            });
        }
    }
    const deleteChattings = () => {
        if (chattings.length === 0) return;
        chattings.forEach(item => {
            fetch(`${process.env.REACT_APP_API_URL}/api/chatGPT/consult?userConsultingId=${item.id}`,
                {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + getCookie('accessToken'),
                    },
                })
                .catch(() => {
                    alert('서버 오류입니다. 잠시 후 다시 시도해주세요.');
                });
        });
    }

    if (diaryData) {
        const dateArray = diaryData.date.split('-');
        const weekDay = new Date(diaryData.date).getDay();
        const formattedDate = `${dateArray[0]}년 ${dateArray[1]}월 ${dateArray[2]}일. ${getDayOfWeek(weekDay)}`;
        const titleLines = diaryData.title.split('\n').map((line, index) => {
            if (index !== diaryData.title.length - 1) {
                return (
                    <div key={index}>
                        {line}
                        <br />
                    </div>
                );
            } else {
                return line;
            }
        });
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
                    <div className="detail-title" contentEditable="false" suppressContentEditableWarning={true}>{titleLines}</div>
                    <textarea className="detail-content" defaultValue={diaryData.content} readOnly/>
                </div>
                <div className={`sentiment-wrapper ${diaryResult.result}`}>
                    <h3 className='sentiment-title'>
                        감정 분석 결과는
                        {diaryResult.result === 'positive' && <span className='title-positive'>긍정적</span>}
                        {diaryResult.result === 'negative' && <span className='title-negative'>부정적</span>}
                        {diaryResult.result === 'neutral' && <span className='title-neutral'>중립적</span>}
                        이네요
                    </h3>
                    <div className='sentiment-content'>{diaryResult.resultAnalysis}</div>
                </div>
                {chattings.length > 0 && (
                    <div className="chatting-wrapper">
                        {chattings.map((chatting, index) => {
                            return (
                                <div key={index}>
                                    <div className='question-wrapper'>{chatting.question}</div>
                                    <div className='answer-wrapper'>{chatting.answer}</div>
                                </div>
                            );
                        })}
                    </div>
                    )}
            </div>
        );
    }else {
        return <p className='noDataMessage'>해당 일기를 조회 중입니다....</p>;
    }

}

export default DetailDiary;
