/**
 * @param source source state
 * @param setSource source state setter
 * @param setAnalyser analyser state setter
 * @param setStream stream state setter
 * @param setMedia media state setter
 * @param setOnRec onRec state setter
 * @description 마이크로 녹음하는 함수에요. 전달해야 하는 state들이 많습니다. 녹화를 끝낼 때는 offRecAudio함수를 쓰세요.
 * @todo stream, media, onRec, source, analyzer, audioUrl 의 이름으로 각각 state, setter 만들어 주세요.
 * @author JongHyeok Park <jhpmsucle@gmail.com>
 */
const onRecAudio = (source, setSource, setAnalyser, setStream, setMedia, setOnRec) => {
  const audioCtx = new (window.AudioContext || window.webkitAudioContext)();

  const analyser = audioCtx.createScriptProcessor(0, 1, 1);
  setAnalyser(analyser);

  function makeSound(stream) {
    const source = audioCtx.createMediaStreamSource(stream);
    setSource(source);

    source.connect(analyser);
    analyser.connect(audioCtx.destination);
  };

  navigator.mediaDevices.getUserMedia({ audio: true })
    .then((stream) => {
      const mediaRecoder = new MediaRecorder(stream);
      mediaRecoder.start();
      setStream(stream);
      setMedia(mediaRecoder);
      makeSound(stream);

      analyser.onaudioprocess = function (e) {
        setOnRec(false);
      };
    });
};

/**
 * 
 * @param {*} setAudioUrl audioUrl state setter
 * @param {*} setOnRec  onRec state setter
 * @param {*} analyser analyzer state
 * @param {*} source source state
 * @param {*} media media state
 * @param {*} stream stream state
 * @description 녹음 정지하는 함수입니다. 결과는 onSubmitAudioFile함수 이용하세요.
 * @author JongHyeok Park <jhpmsucle@gmail.com>
 */
const offRecAudio = (setAudioUrl, setOnRec, analyser, source, media, stream) => {
  media.ondataavailable = function (e) {
    setAudioUrl(e.data);
    setOnRec(true);
  };

  stream.getAudioTracks().forEach(function (track) {
    track.stop();
  });

  media.stop();

  analyser.disconnect();
  source.disconnect();
}

/**
 * @param audioUrl audioUrl state
 * @returns {FormData} 바로 서버에 전송 가능한 FormData 객체를 반환
 * @author JongHyeok Park <jhpmsucle@gmail.com>
 */
const onSubmitAudioFile = (audioUrl) => {
  if (audioUrl) {
    console.log(URL.createObjectURL(audioUrl));
  }

  const sound = new File([audioUrl], 'record.mp3', { lastModified: new Date().getTime(), type: 'audio/mp3' });
  console.log(sound);

  const formData = new FormData();
  formData.append('file', sound);

  return formData;
};


export { onRecAudio, offRecAudio, onSubmitAudioFile };