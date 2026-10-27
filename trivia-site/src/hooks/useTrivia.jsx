import { useState, useEffect } from 'react';

export function useTrivia(initialSettings) {
    const [questions, setQuestions] = useState([]);
    const [token, setToken] = useState(null);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [userAnswers, setUserAnswers] = useState([]);
    const [score, setScore] = useState(null);
    const [status, setStatus] = useState('loading');
    const [errorMessage, setErrorMessage] = useState('');
    const [settings, setSettings] = useState(initialSettings);

    const fetchQuestions = async (currentSettings) => {
        try {
            setStatus('loading');
            setErrorMessage('');

            const params = new URLSearchParams();
            params.append('amount', currentSettings.amount);

            if (currentSettings.category !== 0) {
                params.append('category', currentSettings.category);
            }
            if (currentSettings.difficulty !== 'any') {
                params.append('difficulty', currentSettings.difficulty);
            }

            const response = await fetch(`/questions?${params.toString()}`);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || 'Error connecting to server.');
            }

            setQuestions(data.results || []);
            setToken(data.token);
            setStatus('playing');
        } catch (error) {
            console.error("Failed to load questions:", error);
            setErrorMessage(error.message);
            setStatus('error');
        }
    };

    useEffect(() => {
        fetchQuestions(settings);
    }, []);

    useEffect(() => {
        async function submitGame() {
            if (questions.length > 0 && userAnswers.length === questions.length) {
                setStatus('submitting');
                try {
                    const response = await fetch('/checkanswers', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ token: token, answers: userAnswers })
                    });

                    const result = await response.json();

                    if (!response.ok) {
                        throw new Error(result.error || 'Failed to check answers.');
                    }

                    setScore(result.score);
                    setStatus('finished');
                } catch (error) {
                    console.error("Failed to submit answers:", error);
                    setErrorMessage(error.message);
                    setStatus('error');
                }
            }
        }

        submitGame();
    }, [userAnswers, questions.length, token]);

    function answerQuestion(selectedOption) {
        setUserAnswers(prev => [...prev, selectedOption]);
        setCurrentIndex(prev => prev + 1);
    }

    function restartGame(newSettings = settings) {
        setSettings(newSettings);
        setCurrentIndex(0);
        setUserAnswers([]);
        setScore(null);
        fetchQuestions(newSettings);
    }

    return {
        questions,
        currentIndex,
        score,
        status,
        errorMessage,
        settings,
        answerQuestion,
        restartGame,
    };
}