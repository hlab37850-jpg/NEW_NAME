class InventoryItem {
  final String name;
  final double quantity;
  final String unit;
  final String warehouse;

  InventoryItem({
    required this.name,
    required this.quantity,
    required this.unit,
    required this.warehouse,
  });

  factory InventoryItem.fromRow(List<dynamic> row) {
    String rawQty = row[0].toString().replaceAll(',', '.').trim();
    double parsedQty = double.tryParse(rawQty) ?? 0.0;

    return InventoryItem(
      quantity: parsedQty,
      unit: row[1].toString().trim(),
      name: row[2].toString().trim(),
      warehouse: row[3].toString().trim(),
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'quantity': quantity,
      'unit': unit,
      'warehouse': warehouse,
    };
  }
}
