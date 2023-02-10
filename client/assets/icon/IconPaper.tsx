import React from "react";

interface Props {
  width?: string;
  heigth?: string;
}

export function IconPaper({ width, heigth }: Props) {
  return (
    <svg
      width={width}
      height={heigth}
      viewBox="0 0 25 25"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M22.7627 4.46387H21.4335C21.4322 4.46387 21.4309 4.4644 21.4299 4.46534C21.429 4.46628 21.4285 4.46756 21.4285 4.46889V21.4282C21.4285 21.9018 21.6166 22.356 21.9515 22.6908C22.2864 23.0257 22.7406 23.2139 23.2142 23.2139C23.6878 23.2139 24.142 23.0257 24.4769 22.6908C24.8118 22.356 24.9999 21.9018 24.9999 21.4282V6.70103C24.9999 6.1077 24.7642 5.53867 24.3446 5.11912C23.9251 4.69957 23.3561 4.46387 22.7627 4.46387Z"
        fill="#424242"
      />
      <path
        d="M19.6429 21.4286V2.23214C19.6429 1.93901 19.5851 1.64876 19.473 1.37794C19.3608 1.10712 19.1964 0.861053 18.9891 0.65378C18.7818 0.446506 18.5357 0.282088 18.2649 0.169912C17.9941 0.0577361 17.7038 0 17.4107 0H2.23214C1.64014 0 1.07239 0.235172 0.65378 0.65378C0.235172 1.07239 0 1.64014 0 2.23214V21.875C0 22.7038 0.32924 23.4987 0.915292 24.0847C1.50134 24.6708 2.2962 25 3.125 25H22.2573C22.2657 25.0001 22.2741 24.9985 22.2819 24.9953C22.2897 24.9921 22.2968 24.9874 22.3028 24.9814C22.3088 24.9754 22.3135 24.9683 22.3167 24.9605C22.3199 24.9527 22.3215 24.9443 22.3214 24.9358C22.3214 24.9218 22.3167 24.9082 22.3082 24.8971C22.2996 24.886 22.2876 24.878 22.274 24.8745C21.5187 24.6673 20.8523 24.218 20.377 23.5955C19.9017 22.973 19.6438 22.2118 19.6429 21.4286ZM3.57143 5.35714C3.57143 5.12034 3.6655 4.89324 3.83294 4.7258C4.00038 4.55836 4.22749 4.46429 4.46429 4.46429H8.03572C8.27252 4.46429 8.49962 4.55836 8.66706 4.7258C8.83451 4.89324 8.92857 5.12034 8.92857 5.35714V8.92857C8.92857 9.16537 8.83451 9.39248 8.66706 9.55992C8.49962 9.72736 8.27252 9.82143 8.03572 9.82143H4.46429C4.22749 9.82143 4.00038 9.72736 3.83294 9.55992C3.6655 9.39248 3.57143 9.16537 3.57143 8.92857V5.35714ZM15.1786 20.5357H4.4894C4.00893 20.5357 3.59654 20.1663 3.57255 19.6858C3.56673 19.5651 3.58548 19.4445 3.62767 19.3312C3.66986 19.2179 3.7346 19.1144 3.81798 19.0269C3.90136 18.9394 4.00163 18.8698 4.11273 18.8222C4.22382 18.7746 4.34342 18.75 4.46429 18.75H15.1535C15.6339 18.75 16.0463 19.1194 16.0703 19.5999C16.0761 19.7206 16.0574 19.8413 16.0152 19.9545C15.973 20.0678 15.9083 20.1713 15.8249 20.2588C15.7415 20.3463 15.6412 20.416 15.5301 20.4636C15.419 20.5112 15.2994 20.5357 15.1786 20.5357ZM15.1786 16.9643H4.4894C4.00893 16.9643 3.59654 16.5949 3.57255 16.1144C3.56673 15.9937 3.58548 15.873 3.62767 15.7598C3.66986 15.6465 3.7346 15.543 3.81798 15.4555C3.90136 15.368 4.00163 15.2983 4.11273 15.2507C4.22382 15.2031 4.34342 15.1786 4.46429 15.1786H15.1535C15.6339 15.1786 16.0463 15.548 16.0703 16.0285C16.0761 16.1492 16.0574 16.2698 16.0152 16.3831C15.973 16.4964 15.9083 16.5999 15.8249 16.6874C15.7415 16.7749 15.6412 16.8445 15.5301 16.8921C15.419 16.9397 15.2994 16.9643 15.1786 16.9643ZM15.1786 13.3929H4.4894C4.00893 13.3929 3.59654 13.0234 3.57255 12.543C3.56673 12.4222 3.58548 12.3016 3.62767 12.1883C3.66986 12.0751 3.7346 11.9716 3.81798 11.8841C3.90136 11.7966 4.00163 11.7269 4.11273 11.6793C4.22382 11.6317 4.34342 11.6072 4.46429 11.6071H15.1535C15.6339 11.6071 16.0463 11.9766 16.0703 12.457C16.0761 12.5778 16.0574 12.6984 16.0152 12.8117C15.973 12.9249 15.9083 13.0284 15.8249 13.1159C15.7415 13.2034 15.6412 13.2731 15.5301 13.3207C15.419 13.3683 15.2994 13.3929 15.1786 13.3929ZM15.1786 9.82143H11.6323C11.1518 9.82143 10.7394 9.45201 10.7154 8.97154C10.7096 8.85082 10.7283 8.73017 10.7705 8.61691C10.8127 8.50365 10.8775 8.40014 10.9608 8.31264C11.0442 8.22514 11.1445 8.15548 11.2556 8.10788C11.3667 8.06028 11.4863 8.03573 11.6071 8.03572H15.1535C15.6339 8.03572 16.0463 8.40514 16.0703 8.8856C16.0761 9.00633 16.0574 9.12698 16.0152 9.24024C15.973 9.3535 15.9083 9.45701 15.8249 9.54451C15.7415 9.63201 15.6412 9.70167 15.5301 9.74927C15.419 9.79687 15.2994 9.82142 15.1786 9.82143ZM15.1786 6.25H11.6323C11.1518 6.25 10.7394 5.88058 10.7154 5.40011C10.7096 5.27939 10.7283 5.15874 10.7705 5.04548C10.8127 4.93222 10.8775 4.82871 10.9608 4.74121C11.0442 4.65371 11.1445 4.58405 11.2556 4.53645C11.3667 4.48885 11.4863 4.4643 11.6071 4.46429H15.1535C15.6339 4.46429 16.0463 4.83371 16.0703 5.31418C16.0761 5.4349 16.0574 5.55555 16.0152 5.66881C15.973 5.78207 15.9083 5.88558 15.8249 5.97308C15.7415 6.06058 15.6412 6.13024 15.5301 6.17784C15.419 6.22544 15.2994 6.24999 15.1786 6.25Z"
        fill="#424242"
      />
    </svg>
  );
}