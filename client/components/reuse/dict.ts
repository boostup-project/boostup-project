interface AddDict {
  [index: string]: number;
  강남구: number;
  강동구: number;
  강북구: number;
  강서구: number;
  관악구: number;
  광진구: number;
  구로구: number;
  금천구: number;
  노원구: number;
  도봉구: number;
  동대문구: number;
  동작구: number;
  마포구: number;
  서대문구: number;
  서초구: number;
  성동구: number;
  성북구: number;
  송파구: number;
  양천구: number;
  영등포구: number;
  용산구: number;
  은평구: number;
  종로구: number;
  중구: number;
  중랑구: number;
}
interface LangDict {
  [index: string]: number;
  Javascript: number;
  Python: number;
  Go: number;
  Java: number;
  Kotlin: number;
  PHP: number;
  "C#": number;
  Swift: number;
}
export const addDict: AddDict = {
  강남구: 1,
  강동구: 2,
  강북구: 3,
  강서구: 4,
  관악구: 5,
  광진구: 6,
  구로구: 7,
  금천구: 8,
  노원구: 9,
  도봉구: 10,
  동대문구: 11,
  동작구: 12,
  마포구: 13,
  서대문구: 14,
  서초구: 15,
  성동구: 16,
  성북구: 17,
  송파구: 18,
  양천구: 19,
  영등포구: 20,
  용산구: 21,
  은평구: 22,
  종로구: 23,
  중구: 24,
  중랑구: 25,
};

export const langDict: LangDict = {
  Javascript: 1,
  Python: 2,
  Go: 3,
  Java: 4,
  Kotlin: 5,
  PHP: 6,
  "C#": 7,
  Swift: 8,
};


export const detailLangDict = [
  {
    id: 1,
    name: "javaScript",
    img: "/images/javascript.png",
  },
  {
    id: 2,
    name: "python",
    img: "/images/python.png",
  },
  {
    id: 3,
    name: "go",
    img: "/images/go.png",
  },
  {
    id: 4,
    name: "java",
    img: "/images/java.png",
  },
  {
    id: 5,
    name: "kotlin",
    img: "/images/kotlin.png",
  },
  {
    id: 6,
    name: "php",
    img: "/images/php.png",
  },
  {
    id: 7,
    name: "C#",
    img: "/images/C.png",
  },
  {
    id: 8,
    name: "swift",
    img: "/images/swift.png",
  },
];