const Pagination = ({ total, limit, page, setPage }: any) => {
  const numPages = Math.ceil(total / limit);

  return (
    <>
      <nav className="flex justify-center items-center gap-1 m-4">
        <button
          className="rounded-lg p-2 bg-pointColor text-white text-base disabled:bg-borderColor disabled:cursor-not-allowed"
          onClick={() => setPage(page - 1)}
          disabled={page === 1}
        >
          {" "}
          &lt;
        </button>
        {Array(numPages)
          .fill(undefined)
          .map((_, i) => (
            <button
              className="rounded-lg p-2 bg-pointColor text-white text-base hover:translate-y-[-4px] "
              key={i + 1}
              onClick={() => setPage(i + 1)}
              aria-current={page === i + 1 && "page"}
            >
              {i + 1}
            </button>
          ))}
        <button
          className="rounded-lg p-2 bg-pointColor text-white text-base disabled:bg-borderColor disabled:cursor-not-allowed"
          onClick={() => setPage(page + 1)}
          disabled={page === numPages}
        >
          {" "}
          &gt;
        </button>
      </nav>
    </>
  );
};
export default Pagination;
