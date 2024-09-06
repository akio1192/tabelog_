let maxDate = new Date();
maxDate.setMonth(maxDate.getMonth() + 3);

flatpickr('#fromReservationDate', {
  mode: "single",  // 単一日付選択に設定
  locale: 'ja',
  minDate: 'today',
  maxDate: maxDate,
  dateFormat: "Y-m-d",  // 表示形式を指定
});