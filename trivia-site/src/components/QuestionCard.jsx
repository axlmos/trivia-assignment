export default function QuestionCard({ questionData, currentIndex, totalQuestions, onAnswer }) {
    if (!questionData) return null;

    return (
        <>
            <p>Question {currentIndex + 1} of {totalQuestions}</p>
            <h2 dangerouslySetInnerHTML={{ __html: questionData.question }}></h2>

            <div className="options-grid">
                {questionData.answers.map((answer, index) => (
                    <button
                        key={index}
                        onClick={() => onAnswer(answer)}
                        className="button"
                        dangerouslySetInnerHTML={{ __html: answer }}
                    ></button>
                ))}
            </div>
        </>
    );
}