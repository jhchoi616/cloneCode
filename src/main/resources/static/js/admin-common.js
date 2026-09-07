
/* ============================================================
   B.POINT ADMIN — Common JS
   - 화면 공통 기능만 관리
   - 데이터 처리는 Spring Boot + JPA에서 담당
   ============================================================ */


/* ========= UTIL ========= */

function fmtDate(iso) {
  if (!iso) return '';

  const d = new Date(iso);

  if (isNaN(d)) return iso;

  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');

  return `${y}.${m}.${dd}`;
}


function fmtDateTime(iso) {
  if (!iso) return '';

  const d = new Date(iso);

  if (isNaN(d)) return iso;

  return d.toLocaleString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
}
