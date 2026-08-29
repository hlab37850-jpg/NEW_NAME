import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:smart_assistant/main.dart';

void main() {
  testWidgets('Smart Assistant app starts correctly', (
    WidgetTester tester,
  ) async {
    await tester.pumpWidget(const SmartAssistantApp());

    expect(find.byType(MaterialApp), findsOneWidget);
  });
}
