import BasicStep from "../step/BasicStep";
import ExtraStep from "../step/ExtraStep";
import CurrStep from "../step/CurrStep";

const StepNavWrapper = () => {
    return (
        <>
            <div className="flex flex-row w-full h-fit pt-2 pb-8 justify-center items-center">
                {/* 각 스탭 별 색깔을 활성화시키려면 inputStep 전역상태 값을 변경해주면 됨 */}
                {/* /mainAtom/inputStep */}
                <BasicStep />
                <ExtraStep />
                <CurrStep />
            </div>
        </>
    )
}

export default StepNavWrapper;